<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

	

	<script type="text/javascript">
 
	
	 
		$(document).ready(function(){
			
			var counter = 1;
			
			 $('#add').click(function() {	
				 
				 var ingredientsElement = "<tr id='row" + counter + "'>"+
				 "<td>Ingredient: <input name='ingredients[" + counter + "].ingredient' type='text' /></td>" +
				 "<td>Amount: <input name='ingredients[" + counter + "].amount' type='text' /></td>" +
				 "<td>Metric: <input name='ingredients[" + counter + "].metric' type='text' /></td>" +
				 "<td><input id='remove' value='remove' name=" + counter + " type='button' /></td>" +
				 "</tr>"
				 
				 $(ingredientsElement).appendTo('#ingredientsSection');
  		     });
			 
			 $('#remove').live('click', function() {
				 
				 var rowToBeDeletedIndex = $(this).attr("name");
				 $("#row" + rowToBeDeletedIndex).remove();
				 
			 });
			 
			

		 
		    
		  });
	</script>
    
	<form:form modelAttribute="recipe" id="addRecipeForm" method="post"
		action="saveRecipe">
		<table>
			<tbody id="ingredientsSection">
				<tr>
					<td>Name: <input name="name" type="text" /></td>
				</tr>
				
				<tr>
					<td>Author: <input name="author" type="text"/></td>
				</tr>
				
				<tr>
					<td>Link to Author Website: <input name="authorLink" type="text"/></td>
				</tr>
				
				<tr>
					<td>Diet Type: <input name="diet" type="text"/></td>
				</tr>
				
				<tr>				
						<td>Ingredient: <input name="ingredients[0].ingredient" type="text" /></td>
						<td>Amount: <input name="ingredients[0].amount" type="text" /></td>
						<td>Metric: <input name="ingredients[0].metric" type="text" /></td>
				
				</tr>
								
	        </tbody>
		</table>

    <input type="submit" value="Save" />

	</form:form>
	<a href="#" id="add">add</a>
	
	
	<c:if test="${not empty recipe_errors}">
			<c:forEach items="${recipe_errors}" var="recipe_error">
			${recipe_error.message}
		</c:forEach> 
	</c:if>
	
	<c:if test="${not empty ingredient_errors}">
			<c:forEach items="${ingredient_errors}" var="ingredient_error">
			${ingredient_error.message}
		</c:forEach> 
	</c:if>
