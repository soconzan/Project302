/**
 *  header
 */
const searchBtn = document.querySelector('.nav-search');
const searchDiv = document.querySelector('.search');
const header = document.querySelector('header');

// 검색창 hide & show
searchBtn.addEventListener('click', function() {
    const searchDiv = document.querySelector('.search.container');
    const isHidden = searchDiv.classList.contains('hide');
    if (isHidden) {
        searchDiv.classList.remove('hide');
        header.classList.add('header-shadow');
    } else {
        searchDiv.classList.add('hide');
        header.classList.remove('header-shadow');
    }
});

/*
    aside
*/
const newBtn = document.querySelector('#new-btn');
const newLinkBtns = document.querySelector('.new-btn-list');

newBtn.addEventListener('click', function() {
    // const newLinkBtns = document.querySelector('.new-btn-list');
    const isHidden = newLinkBtns.classList.contains('hide');
    if (isHidden) {
        newLinkBtns.classList.remove('hide');
    } else {
        newLinkBtns.classList.add('hide');
    }
})

document.getElementById('top-btn').addEventListener('click', function() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
});