(function(){

		$(document).ready(function(){
			
			 $('a[name*="like"]').click(function() {
				 
				 var hrefName = $(this).attr("name");
				 var href = $(this);
				 var numOfLikes = '#like' + $(this).attr("title");
				 var currentNumOfLikes = "#current" + $(this).attr("title");
				 
				 $.ajax({  
					  type: "POST",  
					  url: "likeARecipe",  
					  data: 'recipeId=' + $(this).attr("title") + '&operation=' + $(this).attr("name"),  
					  success: function(data) {  
						  
						  alert(data);
						  alert(numOfLikes);
						  alert(currentNumOfLikes);
						  alert("data a: " + $(data).find("span").attr("id"));
						  
						 $(numOfLikes).html($(data));
						
						  if(hrefName == 'unlike')
						  {
							  $(href).attr("name","like");
							  $(href).text("like");
						  }else {
							  $(href).attr("name","unlike");
							  $(href).text("unlike");
						  } 
					  },
					  dataType : 'html'
					});  
					return false; 
			 });
		  });

})(jQuery);

