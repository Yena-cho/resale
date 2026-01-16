<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<nav aria-label="Page navigation example">
	<ul class="pagination justify-content-center" id="PageArea2">
	    <c:if test="${map.pager2.curPage > 1}">
        <li class="page-item">
			<a class="page-link" href="#" aria-label="Move to first" onclick="list2('1')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png"></span>
		        <span class="sr-only">Move to first page</span>
	    	</a>
		</li>
		</c:if>
		<c:if test="${map.pager2.curBlock > 1}">
		<li class="page-item">
			<a class="page-link" href="#" aria-label="Previous" onclick="list2('${map.pager2.prevPage}')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png"></span>
		        <span class="sr-only">Previous</span>
		    </a>
		</li>
		</c:if>
		<c:forEach var="num" begin="${map.pager2.blockBegin}" end="${map.pager2.blockEnd}">
			<c:choose>
				<c:when test="${num == map.pager2.curPage}">
					<li class="page-item active"><a class="page-link">${num}</a></li>
				</c:when>
				<c:otherwise>
					<li class="page-item"><a class="page-link" href="#" onclick="list2('${num}')">${num}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>    
		<c:if test="${map.pager2.curBlock < map.pager2.totBlock}">
		<li class="page-item">
			<a class="page-link" href="#" aria-label="Next" onclick="list2('${map.pager2.nextPage}')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png" class="rotate-180-deg"></span>
		        <span class="sr-only">Next</span>
		    </a>
		</li>
		</c:if>
		<c:if test="${map.pager2.curPage < map.pager2.totPage}">
		<li class="page-item">
			<a class="page-link" href="#" aria-label="Next" onclick="list2('${map.pager2.totPage}')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span>
		        <span class="sr-only">Move to last page</span>
		    </a>
		</li>
		</c:if>
    </ul>
</nav>