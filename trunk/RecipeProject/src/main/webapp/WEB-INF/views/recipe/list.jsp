<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<script type="text/javascript">
	$(document).ready(function() {

		$('#lookForRecipes').click(function() {

			var ingredients = $('#searchText').attr("value");

			$.ajax({
				type : "POST",
				url : "searchRecipeByIngredient",
				data : 'ingredient=' + ingredients,
				success : function(data) {
					$("#result").html($(data).find("#recipeList"));
				},
				dataType : 'html'
			});
			return false;
		});
	});
</script>

<div id="page" class="fullscreen">
	<ul class="breadcrumbs">
		<li id="search-box" class="nohover clearfix"><input type="text"
			id="searchText" name="searchInput" /></li>
	</ul>

	<table>

		<tr>
			<td></td>
			<td><a href="#" id="lookForRecipes">search</a></td>
		</tr>
	</table>

	<div id="result">
		<%@include file="recipes.jsp"%>
	</div>
</div>