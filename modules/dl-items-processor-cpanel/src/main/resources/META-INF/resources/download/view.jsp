<%@ page import="com.liferay.portal.kernel.util.PortalUtil" %>
<%@ page import="net.carlosduran.dl.items.processor.constants.MVCCommandNames" %>
<%@ include file="../init.jsp" %>

<portlet:actionURL var="downloadImagesURL" name="<%= MVCCommandNames.DOWNLOAD_ITEMS_ACTION %>"/>

<div class="p-4">
    <clay:container fluid="false">
        <clay:row>
            <clay:col>
                <h2><liferay-ui:message key="download-items"/></h2>
            </clay:col>
        </clay:row>
        <clay:row>
            <clay:col>
                <aui:form
                        enctype="application/x-www-form-urlencoded"
                        method="post"
                        action="${ downloadImagesURL }">
                    <aui:input label="urls.file" name="urls" type="file"/>
                    <aui:button-row>
                        <aui:button type="submit" value="upload"/>
                        <c:if test="${ fileUploaded }">
                            <portlet:resourceURL var="downloadZipFileURL"
                                                 id="<%= MVCCommandNames.DOWNLOAD_ZIP_RESOURCE %>"/>
                            <a target="_blank" href="${ downloadZipFileURL }" class="btn btn-outline-secondary">
                                <svg height="24px" width="24px" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                                     viewBox="0 0 512 512" xml:space="preserve">
								<g>
                                    <path style="fill:#2D527C;" d="M364.745,512c-1.961,0-3.94-0.379-5.823-1.158c-5.687-2.357-9.397-7.907-9.397-14.064V411.47
										c0-8.406,6.817-15.223,15.223-15.223h85.307c6.158,0,11.708,3.71,14.064,9.397c2.355,5.689,1.052,12.236-3.3,16.59l-85.307,85.307
										C372.601,510.453,368.707,512,364.745,512z M379.971,426.693v33.332l33.332-33.332H379.971z"/>
                                    <path style="fill:#2D527C;" d="M297.412,512H83.931c-20.519,0-37.209-16.692-37.209-37.209V37.209C46.721,16.692,63.412,0,83.931,0
										h344.137c20.519,0,37.209,16.692,37.209,37.209v374.26c0,4.037-1.604,7.908-4.459,10.764l-85.307,85.307
										c-5.946,5.943-15.582,5.943-21.53,0c-5.945-5.945-5.945-15.584,0-21.53l80.849-80.849V37.209c0-3.73-3.034-6.764-6.764-6.764
										H83.931c-3.73,0-6.764,3.034-6.764,6.764v437.58c0,3.73,3.034,6.764,6.764,6.764h213.481c8.406,0,15.223,6.817,15.223,15.223
										C312.635,505.182,305.818,512,297.412,512z"/>
                                </g>
                                    <polygon style="fill:#CEE8FA;" points="287.968,236.085 287.968,129.787 224.032,129.787 224.032,236.085 152.268,236.085
									256,382.213 359.732,236.085 "/>
                                    <path style="fill:#2D527C;" d="M256,397.434c-4.932,0-9.558-2.388-12.413-6.412L139.856,244.896
									c-3.296-4.643-3.725-10.738-1.111-15.797c2.614-5.059,7.832-8.237,13.526-8.237h56.541v-91.074c0-8.406,6.817-15.223,15.223-15.223
									h63.936c8.406,0,15.223,6.817,15.223,15.223v91.074h56.541c5.693,0,10.912,3.179,13.526,8.237
									c2.614,5.059,2.184,11.154-1.111,15.797L268.416,391.022C265.558,395.046,260.932,397.434,256,397.434z M181.744,251.308
									L256,355.914l74.256-104.606h-42.288c-8.406,0-15.223-6.817-15.223-15.223v-91.074h-33.49v91.074
									c0,8.406-6.817,15.223-15.223,15.223H181.744z"/>
								</svg>
                            </a>
                        </c:if>
                    </aui:button-row>
                    <aui:input type="hidden" value="<%= PortalUtil.getCurrentURL(renderRequest) %>" name="redirect"></aui:input>
                </aui:form>
            </clay:col>
        </clay:row>
    </clay:container>
</div>