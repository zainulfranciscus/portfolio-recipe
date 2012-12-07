<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

	<script type="text/javascript">
 
		$(document).ready(function(){
			
			 $('a[name*="like"]').click(function() {
				 
				 var hrefName = $(this).attr("name");
				 var href = $(this);
				 $.ajax({  
					  type: "POST",  
					  url: "likeARecipe",  
					  data: 'recipeId=' + $(this).attr("title") + '&operation=' + $(this).attr("name"),  
					  success: function() {  
						
						  if(hrefName == 'unlike')
						  {
							  $(href).attr("name","like");
							  $(href).text("like");
						  }else {
							  $(href).attr("name","unlike");
							  $(href).text("unlike");
						  } 
					  }  
					});  
					return false; 
			 });
			 
			 
		
		  });
	</script>
	
	<c:if test="${not empty recipes}">
	<table>
		<thead>
			<tr>
				<th>Name</th>
				<th>Author</th>
				<th>diet</th>	
			</tr>
		</thead>
			
		<c:forEach items="${recipes}" var="recipe">
					
			<c:set var="picture" value="images/${recipe.picture}"/>
		
			<tr>
			    <td><img src="${picture}"/></td>			    
				<td><a href="${recipe.id}">${recipe.name}</a> </td>
				<td>${recipe.author}</td>
				<td>${recipe.diet}</td>		
				<sec:authorize access="isAuthenticated()">	
				
				<c:set var="username" value='${sessionScope.RecipeUser.username}'/>
				<c:set var="isLikedByUser" value='${recipe.isLikedByUser(username)}'/>
								
				<c:if test="${isLikedByUser}">
				<td><a href="#" title="${recipe.id}" name="unlike">Unlike</a></td>
				</c:if> 
				
				<c:if test="${not isLikedByUser}">
				<td><a href="#" title="${recipe.id}" name="like">Like</a></td>
				</c:if>
								
				
				</sec:authorize>		
			</tr>
		</c:forEach>
	</table>
	</c:if>
