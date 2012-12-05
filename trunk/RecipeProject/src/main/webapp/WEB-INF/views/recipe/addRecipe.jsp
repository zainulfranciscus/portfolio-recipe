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
						<td>Ingredient: <input name="ingredients[0].ingredient" type="text" /></td>
						<td>Amount: <input name="ingredients[0].amount" type="text" /></td>
						<td>Metric: <input name="ingredients[0].metric" type="text" /></td>
				
				</tr>
								
	        </tbody>
		</table>

    <input type="submit" value="Save" />

	</form:form>
	<a href="#" id="add">add</a>
