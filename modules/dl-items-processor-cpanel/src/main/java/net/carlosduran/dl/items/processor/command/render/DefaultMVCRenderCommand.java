package net.carlosduran.dl.items.processor.command.render;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class DefaultMVCRenderCommand implements MVCRenderCommand {
    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException {
        return "/view.jsp";
    }
}
