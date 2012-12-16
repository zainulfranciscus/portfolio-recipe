<table id="recipeList">
<c:if test="${not empty recipes}">
	
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
	
</c:if>

<c:if test="${empty recipes}">
<tr><td>No Recipe found with these Ingredients</td></tr>
</c:if>
</table>