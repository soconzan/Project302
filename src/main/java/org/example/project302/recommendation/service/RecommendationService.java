package org.example.project302.recommendation.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.example.project302.product.entity.Product;
import org.example.project302.product.repository.ProductRepository;
import org.example.project302.recommendation.dto.RecommendationResponse;
import org.example.project302.recommendation.entity.UserInteraction;
import org.example.project302.recommendation.repository.UserInteractionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendationService {

    private final ProductRepository productRepository;
    private final UserInteractionRepository userInteractionRepository;

    public RecommendationService(ProductRepository productRepository, UserInteractionRepository userInteractionRepository) {
        this.productRepository = productRepository;
        this.userInteractionRepository = userInteractionRepository;
    }

    public List<RecommendationResponse> recommendItems(Long userId) {
        List<UserInteraction> interactions = userInteractionRepository.findByUserId(userId);

        log.info("User interactions for user {}: {}", userId, interactions);

        if (interactions.isEmpty()) {
            log.info("No interactions found for user {}", userId);
            return Collections.emptyList();
        }

        // 사용자가 상호작용한 상품 ID와 가중치 맵
        Map<Long, Double> interactedPdIdsWithWeight = interactions.stream()
                .collect(Collectors.toMap(
                        UserInteraction::getPdId,
                        interaction -> UserInteraction.getInteractionWeight(interaction.getInteractionType()),
                        Double::sum // 키 중복 시 가중치를 합산
                ));

        // 모든 상품 가져오기
        List<Product> allProducts = productRepository.findAll();

        // 상품 설명 벡터화 (TF-IDF)
        Map<Long, String> productContents = allProducts.stream()
                .collect(Collectors.toMap(Product::getPdId, Product::getPdContent));

        // 상품 유사도 계산
        Map<Long, Double> similarityScores = calculateSimilarity(interactedPdIdsWithWeight, productContents);

        // 상위 5개 추천 상품 ID 추출
        List<Long> recommendedPdIds = similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Dto로 변환하여 반환
        List<RecommendationResponse> recommendations = recommendedPdIds.stream()
                .map(pdId -> new RecommendationResponse(pdId, getProductById(pdId).getPdName()))
                .collect(Collectors.toList());

        log.info("Recommended items for user {}: {}", userId, recommendations);

        return recommendations;
    }


    private Map<Long, Double> calculateSimilarity(Map<Long, Double> interactedPdIdsWithWeight, Map<Long, String> productContents) {
        // TF-IDF 벡터화 및 코사인 유사도 계산 로직
        // Lucene을 이용한 TF-IDF 계산 및 유사도 계산
        try {
            Directory directory = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            IndexWriter writer = new IndexWriter(directory, config);

            // 모든 상품을 인덱싱
            for (Map.Entry<Long, String> entry : productContents.entrySet()) {
                Document doc = new Document();
                doc.add(new TextField("pdContent", entry.getValue(), Field.Store.YES));
                doc.add(new TextField("pdId", entry.getKey().toString(), Field.Store.YES));
                writer.addDocument(doc);
            }

            writer.close();

            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new BM25Similarity());

            Map<Long, Double> similarityScores = new HashMap<>();

            for (Map.Entry<Long, Double> entry : interactedPdIdsWithWeight.entrySet()) {
                Long pdId = entry.getKey();
                Double weight = entry.getValue();
                String pdContent = productContents.get(pdId);
                QueryParser parser = new QueryParser("pdContent", analyzer);
                Query query = parser.parse(QueryParser.escape(pdContent));

                TopDocs results = searcher.search(query, productContents.size());
                for (ScoreDoc scoreDoc : results.scoreDocs) {
                    Document doc = searcher.doc(scoreDoc.doc);
                    Long resultPdId = Long.parseLong(doc.get("pdId"));
                    double score = scoreDoc.score;

                    similarityScores.put(resultPdId, similarityScores.getOrDefault(resultPdId, 0.0) + (score * weight));
                }
            }

            reader.close();
            directory.close();

            log.info("Similarity scores: {}", similarityScores);

            return similarityScores;
        } catch (Exception e) {
            throw new RuntimeException("Error calculating similarity", e);
        }
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품아이디: " + productId));
    }
}
