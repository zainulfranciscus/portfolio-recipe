<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="page" class="center">
	<div class="dialog full white">
		<h1 class="light-header">Edit Profile</h1>
		<div class="right-body">

			<c:if test="${not empty account_errors}">
				<div id="error">
					<c:forEach items="${account_errors}" var="error">
					${error.message}
				</c:forEach>
				</div>
			</c:if>

			<form:form modelAttribute="account" id="accountForm" method="post"
				action="saveProfile" enctype="multipart/form-data">
				<div id="edit-profile-form" class="dialog inline">
					<div class="line inline">
						<label for="Email">Email :</label>
						<form:input cssClass="recipeField" path="email" type="text"
							id="userEmail" />
					</div>

					<div class="line inline">
						<label for="Password">Author:</label>
						<form:input cssClass="recipeField" path="password" type="text"
							id="password" />
					</div>


					<div class="line inline">
						<label for="User Name">User Name:</label>
						<form:input cssClass="recipeField" path="userName" type="text" />
					</div>

					<div class="line inline">
						<label for="Diet">Bio:</label>
						<form:textarea cssClass="recipeField" path="bio" />
					</div>

					<div class="line inline">
						<label for="Photo">Photo:</label> <input class="recipeField"
							name="picture" type="file" />
					</div>

					<div class="line inline">
						<label for="locations">Location:</label>
						<form:input cssClass="recipeField" path="location" type="text" />
					</div>

					<div class="line inline">
						<label for="locations">Twitter:</label>
						<form:input cssClass="recipeField" path="twitter" type="text" />
					</div>



					        
					<form:hidden path="version" />
					        
					<form:hidden path="authority" />

				</div>

				<input class="awesome large" type="submit" value="Save" />
					

			</form:form>
		</div>
	</div>
</div>




