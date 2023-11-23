package net.carlosduran.dl.items.processor.constants;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import net.carlosduran.dl.items.processor.util.DLItemsProcessorUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + DLItemsProcessorPortletKeys.DLITEMSPROCESSOR,
                "mvc.command.name=" + MVCCommandNames.DOWNLOAD_ZIP_RESOURCE
        },
        service = MVCResourceCommand.class
)
public class DownloadZipFileMVCResourceCommand implements MVCResourceCommand {
    @Override
    public boolean serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws PortletException {
        return DLItemsProcessorUtil.downloadZipFile(resourceRequest, resourceResponse);
    }
}
