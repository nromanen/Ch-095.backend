<!DOCTYPE html>
<html lang="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Upload Files</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="files" multiple>
    <input type="submit" value="Upload Files"/>
</form>
</body>
</html>