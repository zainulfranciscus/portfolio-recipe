<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<div id="page" class="fullscreen">
	<ul class="breadcrumbs dropdown-menu light clearfix">
		<li id="search-box" class="nohover clearfix">			
		 	<input type="text" id="searchText" name="searchInput"  class="acInput" alt="Search recipes" placeholder="Search recipes" autocomplete="off"/>
		 	<a href="#" id="lookForRecipes" class="btn">Search</a>		 	
		 </li>		 
	</ul>

	<c:if test="${show_liked_recipes == true}">
		<div id="marquee">
			<div id="marquee-title">
				<h1>
					<a href="editProfile">${sessionScope.RecipeUser.userAlias}</a>
				</h1>				
			</div>
		</div>
	</c:if>


	<div id="result">
		<%@include file="recipes.jsp"%>
	</div>
	
	<script type="text/javascript">

	(function () {
	    $("#menu").menu({
	        menuIcon: true,
	        buttons: true
	    })
	} (jQuery));
	
	
	$("img.lazy").lazyload({
		effect : "fadeIn"
	})

	$(document).on('ajaxStop', function() {
		
		$("img.lazy");
				
		$("img.lazy").lazyload({
			effect : "fadeIn"
		}).resize(); // resizing triggers the lazy load.
		
		

		
	});

		$(document).ready(function() {
			
			$("img.lazy").lazyload({
				effect : "fadeIn"
			});

			$('#lookForRecipes').click(function() {

				var ingredients = $('#searchText').attr("value");

				$.ajax({
					type : "POST",
					url : "searchRecipeByIngredient",
					data : 'ingredient=' + ingredients,
					success : function(data) {
						
						$("#result").html($(data).find("#recipe-cards"));
						
						
					},
					dataType : 'html'
				});
				return false;
			});
		});
		
	</script>
</div>

