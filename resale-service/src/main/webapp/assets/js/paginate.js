(function(window) {
    var paginate = function(pageNo, totalItemCount, pageSize, pageGroupSize) {
        pageSize = pageSize || 10;
        pageGroupSize = pageGroupSize || 10;
        var firstPage = 1;
        var lastPage = Math.ceil(totalItemCount / pageSize);
        var pageGroup = Math.ceil(pageNo / pageGroupSize);
        var startPage = Math.max(pageGroup * pageGroupSize - pageGroupSize + 1, firstPage);
        var endPage = Math.min(pageGroup * pageGroupSize, lastPage);
        var previousPage = Math.max(startPage -1, firstPage);
        var nextPage = Math.min(endPage +1, lastPage);
        return {
            pageNo : pageNo,
            firstPage : firstPage,
            lastPage : lastPage,
            startPage : startPage,
            endPage : endPage,
            previousPage : previousPage,
            nextPage : nextPage
        };
    };
    
    window.paginate = paginate;
})(window);
