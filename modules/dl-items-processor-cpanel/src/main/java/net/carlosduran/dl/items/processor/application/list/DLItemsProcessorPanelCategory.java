package net.carlosduran.dl.items.processor.application.list;

import net.carlosduran.dl.items.processor.constants.DLItemsProcessorPanelCategoryKeys;

import com.liferay.application.list.BasePanelCategory;
import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

/**
 * @author Carlos
 */
@Component(
	immediate = true,
	property = {
		"panel.category.key=" + PanelCategoryKeys.APPLICATIONS_MENU_APPLICATIONS,
		"panel.category.order:Integer=100"
	},
	service = PanelCategory.class
)
public class DLItemsProcessorPanelCategory extends BasePanelCategory {

	@Override
	public String getKey() {
		return DLItemsProcessorPanelCategoryKeys.CONTROL_PANEL_CATEGORY;
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "category.my-tools");
	}

}