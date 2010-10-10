<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored ="false" %>
<% final int PAGE_SIZE = 100;
   final int PAGE_COUNT = (LinkDataBroker.getCount() / 100) + 1;
%>

<%@ page import="com.beirodhogy.data.rendering.LinkRO" %>
<%@ page import="com.beirodhogy.data.LinkDataBroker" %>
<%@ page import="java.util.List" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->


<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link href='/style.css' media='screen' rel='stylesheet' type='text/css' /> 
    <title>skype-links</title>
  </head>

  <body>
  	<form action="http://<%= request.getServerName() + ":" + request.getServerPort() %>/skypelinks">
	 	<input name="channel" value="0">
	 	<input name="text" type="text">
	 	<input name="magick" type="text">
	 	<input type="submit" value="send">
  	</form>
  	
    <h1>Listing Objects</h1>
	
    <table>
      <tr>
      	<td colspan="3">
      		<ul class="pager">
 <% for(int i=0;i< PAGE_COUNT;i++) {%>
    	  		<li><span><a href="/?page=<%= i %>" title="Page <%= i + 1 %>."><%=  i + 1 %></a></span></li>		
 <% } %>
 			</ul>
      	</td>
      </tr>
      <tr>
        <th>magick</th>
        <th>text</th>
        <th>ch</th>
      </tr>

<% 
	List<LinkRO> links = LinkDataBroker.getPage( request.getParameter("page"), PAGE_SIZE );
	for(LinkRO link : links ) {
%> 
      <tr>
        <td><%= link.getMagick() %></td>
        <td><%= link.getText() %></td>
        <td><%= link.getChannel() %></td>
      </tr>
<%
		} //end of for-each
%>
    </table>
  </body>
</html>
