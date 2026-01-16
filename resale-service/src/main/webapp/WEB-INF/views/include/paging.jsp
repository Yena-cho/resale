<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<nav aria-label="Page navigation example">
	<ul class="pagination justify-content-center" id="PageArea">
	    <c:if test="${map.pager.curPage > 1}">
        <li class="page-item">
			<a class="page-link" href="#" aria-label="Move to first" onclick="list('1')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png"></span>
		        <span class="sr-only">Move to first page</span>
	    	</a>
		</li>
		</c:if>
		<c:if test="${map.pager.curBlock > 1}">
		<li class="page-item">
			<a class="page-link" href="#" aria-label="Previous" onclick="list('${map.pager.prevPage}')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png"></span>
		        <span class="sr-only">Previous</span>
		    </a>
		</li>
		</c:if>
		<c:forEach var="num" begin="${map.pager.blockBegin}" end="${map.pager.blockEnd}">
			<c:choose>
				<c:when test="${num == map.pager.curPage}">
					<li class="page-item active"><a class="page-link">${num}</a></li>
				</c:when>
				<c:otherwise>
					<li class="page-item"><a class="page-link" href="#" onclick="list('${num}')">${num}</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>    
		<c:if test="${map.pager.curBlock < map.pager.totBlock}">
		<li class="page-item">
			<a class="page-link" href="#" aria-label="Next" onclick="list('${map.pager.nextPage}')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-single-arrow.png" class="rotate-180-deg"></span>
		        <span class="sr-only">Next</span>
		    </a>
		</li>
		</c:if>
		<c:if test="${map.pager.curPage < map.pager.totPage}">
		<li class="page-item">
			<a class="page-link" href="#" aria-label="Next" onclick="list('${map.pager.totPage}')">
		        <span aria-hidden="true"><img src="/assets/imgs/common/icon-pagenation-double-arrow.png" class="rotate-180-deg"></span>
		        <span class="sr-only">Move to last page</span>
		    </a>
		</li>
		</c:if>
    </ul>
</nav>