package net.carlosduran.dl.items.processor.application.list;

import net.carlosduran.dl.items.processor.constants.DLItemsProcessorPanelCategoryKeys;
import net.carlosduran.dl.items.processor.constants.DLItemsProcessorPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Carlos
 */
@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=100",
		"panel.category.key=" + DLItemsProcessorPanelCategoryKeys.CONTROL_PANEL_CATEGORY
	},
	service = PanelApp.class
)
public class DLItemsProcessorPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return DLItemsProcessorPortletKeys.DLITEMSPROCESSOR;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + DLItemsProcessorPortletKeys.DLITEMSPROCESSOR + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}