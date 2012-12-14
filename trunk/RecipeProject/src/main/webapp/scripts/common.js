(function(){

		$(document).ready(function(){
			
			 $('a[name*="like"]').click(function() {
				 
				 var hrefName = $(this).attr("name");
				 var href = $(this);
				 $.ajax({  
					  type: "POST",  
					  url: "likeARecipe",  
					  data: 'recipeId=' + $(this).attr("title") + '&operation=' + $(this).attr("name"),  
					  success: function() {  
						
						  if(hrefName == 'unlike')
						  {
							  $(href).attr("name","like");
							  $(href).text("like");
						  }else {
							  $(href).attr("name","unlike");
							  $(href).text("unlike");
						  } 
					  }  
					});  
					return false; 
			 });
		  });

})(jQuery);
