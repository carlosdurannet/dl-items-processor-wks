<%@ page import="net.carlosduran.dl.items.processor.constants.MVCCommandNames" %>
<%@ include file="init.jsp" %>

<portlet:renderURL var="downloadItemsURL">
	<portlet:param name="mvcRenderCommandName" value="<%= MVCCommandNames.DOWNLOAD_ITEMS_VIEW %>"/>
</portlet:renderURL>
<portlet:renderURL var="loadItemsURL">
	<portlet:param name="mvcRenderCommandName" value="<%= MVCCommandNames.LOAD_ITEMS_VIEW %>"/>
</portlet:renderURL>


<div class="p-4">

	<clay:container fluid="false">
		<clay:row>
			<clay:col>
				<a class="btn btn-primary" href="${ downloadItemsURL }">
					<liferay-ui:message key="download-items" />
				</a>
				<a class="btn btn-primary" href="${ loadItemsURL }">
					<liferay-ui:message key="load-items" />
				</a>
			</clay:col>
		</clay:row>
	</clay:container>
</div>