package no.buildit;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Tree;

import static no.buildit.CheckBoxTree.CheckType.PARTIALCHECK;
import static no.buildit.CheckBoxTree.CheckType.UNCHECKED;

/**
 * @author bno
 */
public class CheckBoxTree extends Tree {

    enum CheckType {
        CHECKED {
            @Override
            String getStyle() {
                return "checked";
            }

            @Override
            CheckType toggle() {
                return UNCHECKED;
            }
        },
        PARTIALCHECK {
            @Override
            String getStyle() {
                return "partialcheck";
            }

            @Override
            CheckType toggle() {
                return UNCHECKED;
            }
        },
        UNCHECKED {
            @Override
            String getStyle() {
                return "unchecked";
            }

            @Override
            CheckType toggle() {
                return CHECKED;
            }
        };

        abstract CheckType toggle();

        abstract String getStyle();
    }

    private static final String CAPTION = "caption";
    private final Map<Object, CheckType> selectionMap;


    public CheckBoxTree() {
        addStyleName("checkboxed");
        setSelectable(false);
        setImmediate(true);
        addContainerProperty(CAPTION, String.class, "");
        setItemCaptionPropertyId(CAPTION);
        selectionMap = new HashMap<Object, CheckType>();

        setItemStyleGenerator(itemStyleGenerator);
        addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                CheckType checkType = selectionMap.get(itemId);
                CheckType newCheckType = checkType.toggle();
                selectionMap.put(itemId, newCheckType);
                updateFamily(itemId, newCheckType);
                requestRepaint();
            }
        });
    }

    private ItemStyleGenerator itemStyleGenerator = new ItemStyleGenerator() {
        public String getStyle(Object itemId) {
            return selectionMap.get(itemId).getStyle();
        }
    };

    private void updateFamily(Object itemId, CheckType checkType) {
        updateChildren(itemId, checkType);
        updateFather(itemId);
    }

    private void updateChildren(Object itemId, CheckType checkType) {
        Collection<?> children = getChildren(itemId);
        if (children != null) {
            for (Object child : children) {
                selectionMap.put(child, checkType);
                updateChildren(child, checkType);
            }
        }
    }

    private void updateFather(Object itemId) {
        Object parent = getParent(itemId);
        if (parent == null) {
            return;
        }
        Collection<?> children = getChildren(parent);
        CheckType checkType = resolveCheckType(children);
        selectionMap.put(parent, checkType);
        updateFather(parent);
    }

    private CheckType resolveCheckType(Collection<?> children) {
        Set<CheckType> uniqueValues = new HashSet<CheckType>();
        for (Object child : children) {
            uniqueValues.add(selectionMap.get(child));
        }
        if (uniqueValues.size() == 1) {
            return uniqueValues.iterator().next();
        } else {
            return PARTIALCHECK;
        }
    }

    public Item addNode(String itemId, boolean isChildrenAllowed, boolean expand) {
        return addNode(itemId, itemId, null, isChildrenAllowed, expand);
    }

    public Item addNode(String itemId, String parent, boolean isChildrenAllowed, boolean expand) {
        return addNode(itemId, itemId, parent, isChildrenAllowed, expand);
    }

    public Item addNode(Object itemId, String caption, boolean isChildrenAllowed, boolean expand) {
        return addNode(itemId, caption, null, isChildrenAllowed, expand);
    }

    public Item addNode(Object itemId, String caption, Object parent, boolean isChildrenAllowed, boolean expand) {
        Item item = addItem(itemId);
        setParent(itemId, parent);
        setChildrenAllowed(itemId, isChildrenAllowed);
        if (expand) {
            expandItem(itemId);
        }
        item.getItemProperty(CAPTION).setValue(caption);
        return item;
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        Item item = super.addItem(itemId);
        selectionMap.put(itemId, UNCHECKED);
        return item;
    }
}
