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
			
			 $('input[name*=".ingredientType.name"]').autocomplete({
				 source: ingredientArray, appendTo: '#menu-container'
			 });
			 
			 $('ul.ui-autocomplete').css({
				 'background': 'none repeat scroll 0 0 #FFFFFF',
			     'border-color': '#CCCCCC #DCDCDC #E4E4E4',
			     'border-radius': '5px 5px 5px 5px',
			     'border-style': 'solid',
			     'border-width': '1px',
			     'box-shadow' : '0 0 1px rgba(0, 0, 0, 0.12) inset',
			     'color': '#404038',
			     'font': '1.4em Arial,Helvetica,sans-serif',
			     'padding': '0.4em 0.6em',
			     'transition' : 'all 0.08s ease-in-out 0s'			    			    
			 });
			 
			 $('#add').click(function() {	
				 
				 var ingredientsElement = "<div lang='row" + counter + "' class='line inline'>" +
				 "<label class='ingredientLabel'>"+
				 "<span class='ingredientSpan'>Ingredient:</span>" + 
				 "<input class= 'ingredientField' name='ingredients[" + counter + "].ingredientType.name' type='text' lang='" + counter + "'/>" +
				 "<div id='menu-container"+ counter + "' style='position:absolute; width: 249px;'></div>" +
				 "<input class= 'ingredientField' name='ingredients[" + counter + "].ingredientType.id' type='hidden' /></label></div>" +
				 "<input name='ingredients[" + counter + "].ingredientType.version' type='hidden' value='0'/>" +
				 "<div lang='row" + counter + "' class='line inline'><label class='ingredientLabel'><span class='ingredientSpan'>Amount:</span>" + 
				 "<input  class= 'ingredientField' name='ingredients[" + counter + "].amount' type='text' /></label></div>" +
				 "<div lang='row" + counter + "' class='line inline'><label class='ingredientLabel'><span class='ingredientSpan'>Metric:</span>" +
				 "<input   class= 'ingredientField' name='ingredients[" + counter + "].metric' type='text' /></label></div>" +
				 "<div lang='row" + counter + "' class='line inline'><span class='ingredientSpan'><label class='ingredientLabel'><input value='remove' name='removeBtn' lang=" + counter + " type='button' class='awesome' />" + "</span></label></div><div class='clear-float'/>";
				 
				 $(ingredientsElement).appendTo('#ingredientList');
				 
				 $('input[name*=".ingredientType.name"]').autocomplete({
					 source: ingredientArray, appendTo: '#menu-container' + counter
				 });
				 
				 $('ul.ui-autocomplete').css({
					 'background': 'none repeat scroll 0 0 #FFFFFF',
				     'border-color': '#CCCCCC #DCDCDC #E4E4E4',
				     'border-radius': '5px 5px 5px 5px',
				     'border-style': 'solid',
				     'border-width': '1px',
				     'box-shadow' : '0 0 1px rgba(0, 0, 0, 0.12) inset',
				     'color': '#404038',
				     'font': '1.4em Arial,Helvetica,sans-serif',
				     'padding': '0.4em 0.6em',
				     'transition' : 'all 0.08s ease-in-out 0s'			    			    
				 });
				 
				 counter +=1;
  		     });
			 
			
			
			
			 
			 $('input[name*=".ingredientType.name"]').live('blur',function(){
				 
				 var ingredientTypeFieldName = "ingredients[" +  $(this).attr("lang") + "].ingredientType.id";
				 var ingredientTypeVersion = "ingredients[" +  $(this).attr("lang") + "].ingredientType.version";
				 
				 var ingredientTypeValue = $(this).val();				 
				 var idForThisIngredient = $('input[lang="' + ingredientTypeValue + '"]').val();
				 var versionForThisIngredient = $('input[lang="' + ingredientTypeValue + '"]').attr("accept");
				 
				 $('input[name="' + ingredientTypeFieldName + '"]').attr("value",idForThisIngredient);
				 $('input[name="' + ingredientTypeVersion + '"]').attr("value",versionForThisIngredient);
				 
			 });
			 
			 $('input[name="removeBtn"]').live('click', function() {
				 
				 var rowToBeDeletedIndex = $(this).attr("lang");
				 $("div[lang='row" + rowToBeDeletedIndex + "']").remove();
				 
			 });
			 
			 

		  });
	</script>
	
	<c:forEach items="${ingredientTypes}" var="ingredientType">   
    	<input type="hidden" value="${ingredientType.id}" name="ingredientType" lang="${ingredientType.name}" accept="${ingredientType.version}"/>
    </c:forEach>

	
	
	<div id="page" class="center">
		<div class="dialog full white">
		
			
				<c:if test="${not empty recipe_errors}">
				<div id="error">
				<ul>
						<c:forEach items="${recipe_errors}" var="recipe_error">
						<li>${recipe_error.message}</li>
					</c:forEach> 
				</ul>
				</div>
				</c:if>
				
				<c:if test="${not empty ingredient_errors}">
				<div id="error">
				<ul>
						<c:forEach items="${ingredient_errors}" var="ingredient_error">
						<li>${ingredient_error.message}</li>
					</c:forEach>
				</ul>
				</div> 
				</c:if>
				
				<c:if test="${not empty ingredientType_errors}">
				<div id="error">
				<ul>
					<c:forEach items="${ingredientType_errors}" var="ingredientType_error">
						<li>${ingredientType_errors.message}</li>
					</c:forEach>
				</ul>
				</div>  
				</c:if>
			
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
										<input name='ingredients[0].ingredientType.version' type='hidden' value="0"/>
									</label>
									<div id="menu-container" style="position:absolute; width: 249px;">
									</div>
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
								
								 <input id="add" value="add" type="button" type="button" class="awesome"  />	
											
								<div class="clear-float"/>
						
							</div>
							
							
									
				        </div>				        
				        
							<div class="line inline">
								<label class="ingredientLabel">	
									<input class="awesome large" type="submit" value="Save"/>
								</label>
							</div>	

				</form:form>
			</div>
		</div>
	</div>

	


