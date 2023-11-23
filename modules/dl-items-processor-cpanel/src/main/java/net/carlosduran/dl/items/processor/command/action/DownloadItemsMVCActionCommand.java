package net.carlosduran.dl.items.processor.command.action;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import net.carlosduran.dl.items.processor.constants.DLItemsProcessorPortletKeys;
import net.carlosduran.dl.items.processor.constants.MVCCommandNames;
import net.carlosduran.dl.items.processor.constants.Params;
import net.carlosduran.dl.items.processor.util.DLItemsProcessorUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + DLItemsProcessorPortletKeys.DLITEMSPROCESSOR,
                "mvc.command.name=" + MVCCommandNames.DOWNLOAD_ITEMS_ACTION
        },
        service = MVCActionCommand.class
)
public class DownloadItemsMVCActionCommand extends BaseMVCActionCommand {
    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        DLItemsProcessorUtil.generateOperationDirectory();
        DLItemsProcessorUtil.downloadDLItems(
                DLItemsProcessorUtil.getFilteredUrls(PortalUtil.getUploadPortletRequest(actionRequest)));
        DLItemsProcessorUtil.generateZipFile();
        actionRequest.getPortletSession().setAttribute(Params.FILE_UPLOADED, "true");
        actionResponse.sendRedirect(ParamUtil.getString(actionRequest, "redirect"));
    }

    private static final Log _log = LogFactoryUtil.getLog(DownloadItemsMVCActionCommand.class);
}
