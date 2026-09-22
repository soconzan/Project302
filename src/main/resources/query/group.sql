-- 전체 조회
-- 10km -> 0.1
SELECT
    p.pd_id,
    p.pd_name,
    p.pd_content,
    p.pd_price,
    p.creat_date,
    p.pull_up_date,
    p.latitude,
    p.longitude,
    p.views,
    c.ctgr_name,
    g.min_people,
    g.max_people,
    g.round_yn,
    g.clos_date,
    f.file_id,
    un.univ_addr,
    un.univ_latitude,
    un.univ_longitude,
    COALESCE(COUNT(cp.chat_id), 0) AS chat_count
FROM
    users u
        JOIN
    products p ON u.user_id = p.user_id
        JOIN
    categories c ON c.ctgr_id = p.ctgr_id
        LEFT JOIN
    group_details g ON p.pd_id = g.pd_id
        JOIN
    files f ON f.pd_id = p.pd_id AND f.thumbnail_yn = 1
        LEFT JOIN
    chatrooms cr ON p.pd_id = cr.pd_id
        LEFT JOIN
    chat_participations cp ON cp.chat_id = cr.chat_id
        JOIN
    universities un ON u.univ_id = un.univ_id
where un.univ_id = 517
  AND p.pd_status = 'GROUP'
  AND (1 IS NULL OR c.ctgr_id = 1)
  and g.clos_date > now()
GROUP BY
    p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.creat_date, p.pull_up_date,
    p.latitude, p.longitude, c.ctgr_name, g.min_people, g.max_people,
    g.round_yn, g.clos_date, f.file_id, un.univ_addr
ORDER BY
    p.creat_date;

-- ott 전체 조회
SELECT
    p.pd_id,
    p.pd_name,
    p.pd_content,
    p.pd_price,
    p.creat_date,
    p.pull_up_date,
    p.views,
    c.ctgr_name,
    g.min_people,
    g.max_people,
    g.round_yn,
    g.clos_date,
    un.univ_addr,
    COALESCE(COUNT(cp.chat_id), 0) AS chat_count
FROM
    users u
        JOIN
    products p ON u.user_id = p.user_id
        JOIN
    categories c ON c.ctgr_id = p.ctgr_id
        LEFT JOIN
    group_details g ON p.pd_id = g.pd_id
        left JOIN
    chatrooms cr ON p.pd_id = cr.pd_id
        LEFT JOIN
    chat_participations cp ON cp.chat_id = cr.chat_id
        JOIN
    universities un ON u.univ_id = un.univ_id
where un.univ_id = 517
  AND p.pd_status = 'GROUP'
  and g.clos_date > now()
  and c.ctgr_id = 12
GROUP BY
    p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.creat_date, p.pull_up_date,
    p.latitude, p.longitude, c.ctgr_name, g.min_people, g.max_people,
    g.round_yn, g.clos_date, un.univ_addr
ORDER BY
    p.creat_date;

--한 건 조회
SELECT
    p.pd_id,
    p.pd_name,
    p.pd_content,
    p.pd_price,
    p.creat_date,
    p.pull_up_date,
    p.latitude,
    p.longitude,
    p.views,
    p.detail_addr,
    p.deal_status,
    c.ctgr_name,
    g.min_people,
    g.max_people,
    g.round_yn,
    g.clos_date,
    TIMESTAMPDIFF(DAY, NOW(), g.clos_date) as d_day,
    un.univ_addr,
    u.nickname,
    COALESCE(COUNT(DISTINCT l.like_pd), 0) AS like_count,
    COALESCE(COUNT(cp.chat_id), 0) AS chat_count
FROM
    users u
        JOIN
    products p ON u.user_id = p.user_id
        JOIN
    categories c ON c.ctgr_id = p.ctgr_id
        LEFT JOIN
    group_details g ON p.pd_id = g.pd_id
        LEFT JOIN
    likes l ON p.pd_id = l.like_pd
        LEFT JOIN
    chatrooms cr ON p.pd_id = cr.pd_id
        LEFT JOIN
    chat_participations cp ON cp.chat_id = cr.chat_id
        JOIN
    universities un ON u.univ_id = un.univ_id
WHERE
        un.univ_id = 517
  AND p.pd_id = 77
GROUP BY
    p.pd_id, p.pd_name, p.pd_content, p.pd_price, p.creat_date, p.pull_up_date,
    p.latitude, p.longitude, p.views, p.detail_addr, p.deal_status, c.ctgr_name,
    g.min_people, g.max_people, g.round_yn, g.clos_date, un.univ_addr, u.nickname;
