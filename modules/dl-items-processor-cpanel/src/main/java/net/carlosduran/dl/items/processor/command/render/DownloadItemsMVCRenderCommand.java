package net.carlosduran.dl.items.processor.command.render;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import net.carlosduran.dl.items.processor.constants.DLItemsProcessorPortletKeys;
import net.carlosduran.dl.items.processor.constants.MVCCommandNames;
import net.carlosduran.dl.items.processor.constants.Params;
import net.carlosduran.dl.items.processor.util.DLItemsProcessorUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + DLItemsProcessorPortletKeys.DLITEMSPROCESSOR,
                "mvc.command.name=" + MVCCommandNames.DOWNLOAD_ITEMS_VIEW
        },
        service = MVCRenderCommand.class
)
public class DownloadItemsMVCRenderCommand implements MVCRenderCommand {

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
        boolean fileUploaded = DLItemsProcessorUtil.getBoolean(renderRequest, Params.FILE_UPLOADED);
        _log.info("File Uploaded: " + fileUploaded);
        renderRequest.setAttribute(
                Params.FILE_UPLOADED, fileUploaded);

        return "/download/view.jsp";
    }

    private static final Log _log = LogFactoryUtil.getLog(DownloadItemsMVCRenderCommand.class);

}
