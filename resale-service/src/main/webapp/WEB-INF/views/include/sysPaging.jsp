<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row m-b-lg">
   <div class="col-lg-12 text-center">
       <div class="btn-group" id="PageArea">
		    <c:if test="${map.pager.curBlock > 1}">
				<button type="button" class="btn btn-white" onclick="list('1')"><i class="fa fa-chevron-left"></i></button>
			</c:if>
			<c:if test="${map.pager.curBlock > 1}">
			<li class="page-item">
			    <button type="button" class="btn btn-white" onclick="list('1')"><i class="fa fa-chevron-left"></i></button>
			</li>
			</c:if>
			<c:forEach var="num" begin="${map.pager.blockBegin}" end="${map.pager.blockEnd}">
				<c:choose>
					<c:when test="${num == map.pager.curPage}">
						<button class="btn btn-white  active" id="sysCurPage">${num}</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-white" onclick="list('${num}')">${num}</button>
					</c:otherwise>
				</c:choose>
			</c:forEach>    
			<c:if test="${map.pager.curBlock < map.pager.totBlock}">
				<button type="button" class="btn btn-white" onclick="list('${map.pager.nextPage}')"><i class="fa fa-chevron-right"></i> </button>
			</c:if>
		</div>
	</div>
</div>