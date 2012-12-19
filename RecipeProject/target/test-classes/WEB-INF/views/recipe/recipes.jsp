<div id="recipe-cards">
	<c:if test="${not empty recipes}">
		<c:forEach items="${recipes}" var="recipe">
			<div class="recipe-card">
				<c:set var="picture" value="images/${recipe.picture}" />
				<a href="${recipe.id}"> 
					<div class="img-n-title">
						<div class="img-n-overlay">
							<img class="thumb" src="${picture}"/>
							<div class="overlay"></div>
						</div>
						<div class="title">${recipe.name}</div> 
					</div>
					
					<div class="card-footer">
						<div class="left">
							<div class="domain">
								${recipe.author}
							</div>
							
							<div class="byline">
							
							<a href="#" title="${recipe.id}" name="like" class="metric svc clickable">Like</a>
							
							<c:if test="${recipe.diet  == 'Vegan'}">
								<img class="diet" src="images/vegan_icon.png" title="Vegan">
							</c:if>
							</div>
						</div>					
					</div>
					
					  <sec:authorize
						access="isAuthenticated()">

						<c:set var="username" value='${sessionScope.RecipeUser.username}' />
						<c:set var="isLikedByUser"
							value='${recipe.isLikedByUser(username)}' />

						<c:if test="${isLikedByUser}">
							<a href="#" title="${recipe.id}" name="unlike">Unlike</a>
						</c:if>

						<c:if test="${not isLikedByUser}">
							<a href="#" title="${recipe.id}" name="like">Like</a>
						</c:if>

					</sec:authorize>
				</a>
			</div>
		</c:forEach>

	</c:if>

	<c:if test="${empty recipes}">
No Recipe found with these Ingredient
</c:if>
</div>