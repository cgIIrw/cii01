<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="exe.JavaClassExecuter" %>
<%
    InputStream is = new FileInputStream("/Users/yin/IdeaProjects/cii01/out/artifacts/cii01/WEB-INF/classes/TestClass.class");
    byte[] b = new byte[is.available()];
    is.read(b);
    is.close();
//    String text = exe.JavaClassExecuter.execute(b);
    out.println(JavaClassExecuter.execute(b));
%>