<c:set var="username" value='${sessionScope.RecipeUser.username}' />



<div id="recipe-cards">
	<c:if test="${not empty recipes}">
		<c:forEach items="${recipes}" var="recipe">
			<div class="recipe-card">
				<c:set var="picture" value="images/${recipe.picture}" />
				<a href="${recipe.id}"> 
					<div class="img-n-title">
						<div class="img-n-overlay">
							<spring:url value='/img_resources/thumb${recipe.picture}' var="recipe_picture"/>							
							<img class="lazy" src="images/blank.gif" data-original="${recipe_picture}"/>
							<noscript><img src="${recipe_picture}" /></noscript>
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
							 <sec:authorize access="isAuthenticated()">
							<c:set var="isLikedByUser" value='${recipe.isLikedByUser(username)}' />
								
								<c:if test="${not empty username}">
									<c:if test="${isLikedByUser}">
									
										<a class="btn like" href="#" title="${recipe.id}" name="unlike">
											<span>Unlike</span>
										</a>
										
									</c:if>
						
									<c:if test="${not isLikedByUser}">
									
										<a class="btn like" href="#" title="${recipe.id}" name="like">
											<span>Like</span>
										</a>
										
									</c:if>
									<span class="svc clickable">${recipe.numberOfLikes}</span>
								</c:if>		
							 </sec:authorize>
							<c:if test="${recipe.diet  == 'Vegan'}">
								<img class="diet" src="images/vegan_icon.png" title="Vegan">
							</c:if>
							</div>
						</div>					
					</div>
					
				</a>
			</div>
		</c:forEach>

	</c:if>

	<c:if test="${empty recipes}">
		No Recipe found with these Ingredient
	</c:if>
</div>