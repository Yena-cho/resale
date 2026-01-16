<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row m-b-lg">
   <div class="col-lg-12 text-center">
       <div class="btn-group" id="ePageArea">
		    <c:if test="${map.epager.curBlock > 1}">
				<button type="button" class="btn btn-white" onclick="list2('1')"><i class="fa fa-chevron-left"></i></button>
			</c:if>
			<c:if test="${map.epager.curBlock > 1}">
			<li class="page-item">
			    <button type="button" class="btn btn-white" onclick="list2('1')"><i class="fa fa-chevron-left"></i></button>
			</li>
			</c:if>
			<c:forEach var="num" begin="${map.epager.blockBegin}" end="${map.epager.blockEnd}">
				<c:choose>
					<c:when test="${num == map.epager.curPage}">
						<button class="btn btn-white  active" id="sysCurPage">${num}</button>
					</c:when>
					<c:otherwise>
						<button class="btn btn-white" onclick="list2('${num}')">${num}</button>
					</c:otherwise>
				</c:choose>
			</c:forEach>    
			<c:if test="${map.epager.curBlock < map.epager.totBlock}">
				<button type="button" class="btn btn-white" onclick="list2('${map.epager.nextPage}')"><i class="fa fa-chevron-right"></i> </button>
			</c:if>
		</div>
	</div>
</div>