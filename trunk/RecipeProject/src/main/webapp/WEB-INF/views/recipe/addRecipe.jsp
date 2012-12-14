<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

	<script type="text/javascript">
	
 		$(document).ready(function(){
			
			var counter = 1;
			
			var ingredientList =  $('input[name="ingredientType"]');
			var ingredientArray	 = new Array();
			
			for(var i = 0; i < ingredientList.length; i++)
			{
				ingredientArray[i] = $(ingredientList[i]).attr("lang");
				
			}
			
			 $('#add').click(function() {	
				 
				 var ingredientsElement = "<tr id='row" + counter + "'>"+
				 "<td>Ingredient: <input name='ingredients[" + counter + "].ingredientType.name' type='text' lang='" + counter + "'/>" +
				 "<input name='ingredients[" + counter + "].ingredientType.id' type='hidden' /></td>" +
				 "<td>Amount: <input name='ingredients[" + counter + "].amount' type='text' /></td>" +
				 "<td>Metric: <input name='ingredients[" + counter + "].metric' type='text' /></td>" +
				 "<td><input value='remove' name='removeBtn' lang=" + counter + " type='button' /></td>" +
				 "</tr>"
				 
				 $(ingredientsElement).appendTo('#ingredientsSection');
				 
				 counter +=1;
  		     });
			 
			 $('input[name*=".ingredientType.name"]').autocomplete({
				 source: ingredientArray
			 });
			 
			 $('input[name*=".ingredientType.name"]').live('blur',function(){
				 
				 var ingredientTypeFieldName = "ingredients[" +  $(this).attr("lang") + "].ingredientType.id";
				 var ingredientTypeValue = $(this).val();				 
				 var idForThisIngredient = $('input[lang="' + ingredientTypeValue + '"]').val();
				 
				 $('input[name="' + ingredientTypeFieldName + '"]').attr("value",idForThisIngredient);
				 
			 });
			 
			 $('input[name="removeBtn"]').live('click', function() {
				 
				 var rowToBeDeletedIndex = $(this).attr("lang");
				 $("#row" + rowToBeDeletedIndex).remove();
				 
			 });

		  });
	</script>
	
	<c:forEach items="${ingredientTypes}" var="ingredientType">   
    	<input type="hidden" value="${ingredientType.id}" name="ingredientType" lang="${ingredientType.name}"/>
    </c:forEach>
	
    
	<form:form modelAttribute="recipe" id="addRecipeForm" method="post"
		action="saveRecipe" enctype="multipart/form-data">
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
					<td>Photo: <input name="picture" type="text"/></td>
				</tr>
				
				<tr>
					<td>Photo: <input name="file" type="file"/></td>
				</tr>
				
				<tr>				
						<td>
						Ingredient: <input name="ingredients[0].ingredientType.name" lang="0" type="text" />
						<input name='ingredients[0].ingredientType.id' type='hidden'"/>
						</td>
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
	
	<img src="file:///C:/source/Pictures/vegan_spicy_pinto_bean.jpg"></img>
