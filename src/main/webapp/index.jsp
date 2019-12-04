<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <form action="${pageContext.request.contextPath}/fileupload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" >
    <input type="submit" value="Upload Files"/>
  </form>
  </body>
</html>
