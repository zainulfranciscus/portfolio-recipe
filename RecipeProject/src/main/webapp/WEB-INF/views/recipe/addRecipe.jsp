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
				 
				 var ingredientsElement = "<div lang='row" + counter + "' class='line inline'>" +
				 "<label class='ingredientLabel'>"+
				 "<span class='ingredientSpan'>Ingredient:</span>" + 
				 "<input class= 'ingredientField' name='ingredients[" + counter + "].ingredientType.name' type='text' lang='" + counter + "'/>" +
				 "<input class= 'ingredientField' name='ingredients[" + counter + "].ingredientType.id' type='hidden' /></label></div>" +
				 "<div lang='row" + counter + "' class='line inline'><label class='ingredientLabel'><span class='ingredientSpan'>Amount:</span>" + 
				 "<input  class= 'ingredientField' name='ingredients[" + counter + "].amount' type='text' /></label></div>" +
				 "<div lang='row" + counter + "' class='line inline'><label class='ingredientLabel'><span class='ingredientSpan'>Metric:</span>" +
				 "<input   class= 'ingredientField' name='ingredients[" + counter + "].metric' type='text' /></label></div>" +
				 "<div lang='row" + counter + "' class='line inline'><span class='ingredientSpan'><label class='ingredientLabel'><input value='remove' name='removeBtn' lang=" + counter + " type='button' class='awesome' />" + "</span></label></div>";
				 
				 $(ingredientsElement).appendTo('#ingredientList');
				 
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
				 $("div[lang='row" + rowToBeDeletedIndex + "']").remove();
				 
			 });

		  });
	</script>
	
	<c:forEach items="${ingredientTypes}" var="ingredientType">   
    	<input type="hidden" value="${ingredientType.id}" name="ingredientType" lang="${ingredientType.name}"/>
    </c:forEach>

	<div id="page" class="center">
		<div class="dialog full white">
			<h1 class="light-header">Add a Recipe</h1>
			<div class="right-body">
				<form:form modelAttribute="recipe" id="addRecipeForm" method="post" action="saveRecipe" enctype="multipart/form-data">
					
						<div id="ingredientsSection">
						
							<div class="line inline">
								<label for="Name">Name:</label> 
								<input class="recipeField" name="name" type="text" />
							</div>
							
							<div class="line inline">
								<label for="Author">Author:</label> 
							    <input class="recipeField"  name="author" type="text"/>
							</div>
						
							
							<div class="line inline">
								<label for="Author Website">Link to Author Website:</label> 
								<input class="recipeField"  name="authorLink" type="text"/>
							</div>
							
							<div class="line inline">
								<label for="Diet">Diet:</label> 
								<input class="recipeField"  name="diet" type="text"/>
							</div>
							
							<div class="line inline">
								<label for="Photo">Photo:</label>
								<input class="recipeField"  name="file" type="file"/>
							</div>													
							
							
							<div id="ingredientList">
							
								<div class="line inline">
									<label class="ingredientLabel">
										<span class="ingredientSpan">Ingredient:</span>
										<input class="ingredientField" name="ingredients[0].ingredientType.name" lang="0" type="text" />
										<input name='ingredients[0].ingredientType.id' type='hidden'/>
									</label>
								</div>	
								
								<div class="line inline">
									<label class="ingredientLabel">
										<span class="ingredientSpan">Amount:</span> 
										<input class="ingredientField" name="ingredients[0].amount" type="text"  />
									</label>
								</div>
								
								<div class="line inline">		
									<label class="ingredientLabel">		
										 <span class="ingredientSpan">Metric:</span> 
										 <input class="ingredientField" name="ingredients[0].metric" type="text"  />						
									</label>
								</div>
															
								<div class="clear-float"/>
						
							</div>
							
							
									
				        </div>

				</form:form>
			</div>
		</div>
	</div>

	
	
							<div class="line inline">
								<label class="ingredientLabel">	
									<input class="awesome large" type="submit" value="Save"/>
								</label>
							</div>	
							
								
							
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


