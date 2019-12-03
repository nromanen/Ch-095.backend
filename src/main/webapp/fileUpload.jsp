<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form:form  action="/fileUploadPage" method = "POST" modelAttribute = "fileUpload"
            enctype = "multipart/form-data">
    Please select a file to upload :
    <input type = "file" name = "file" />
    <input type = "submit" value = "upload photo" />
</form:form>
</body>
</html>
