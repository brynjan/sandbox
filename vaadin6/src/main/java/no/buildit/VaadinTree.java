package no.buildit;

import com.vaadin.data.Item;

/**
 * This class..
 *
 * @author bno
 */
public class VaadinTree extends CheckBoxTree {

//    enum CheckType {
//        CHECKED {
//            @Override
//            String getStyle() {
//                return "checked";
//            }
//
//            @Override
//            CheckType toggle() {
//                return UNCHECKED;
//            }
//        },
//        PARTIALCHECK {
//            @Override
//            String getStyle() {
//                return "partialcheck";
//            }
//
//            @Override
//            CheckType toggle() {
//                return UNCHECKED;
//            }
//        },
//        UNCHECKED {
//            @Override
//            String getStyle() {
//                return "unchecked";
//            }
//
//            @Override
//            CheckType toggle() {
//                return CHECKED;
//            }
//        };
//
//        abstract CheckType toggle();
//
//        abstract String getStyle();
//    }

    public VaadinTree() {
        setSizeFull();

        fill();
    }

//    private void updateFamily(Object itemId, CheckType checkType) {
//        updateChildren(itemId, checkType);
//        updateFather(itemId);
//    }
//
//    private void updateChildren(Object itemId, CheckType checkType) {
//        Collection<?> children = getChildren(itemId);
//        if (children != null) {
//            for (Object child : children) {
//                selectionMap.put(child, checkType);
//                updateChildren(child, checkType);
//            }
//        }
//    }
//
//    private void updateFather(Object itemId) {
//        Object parent = getParent(itemId);
//        if (parent == null) {
//            return;
//        }
//        Collection<?> children = getChildren(parent);
//        CheckType checkType = resolveCheckType(children);
//        selectionMap.put(parent, checkType);
//        updateFather(parent);
//    }
//
//    private CheckType resolveCheckType(Collection<?> children) {
//        Set<CheckType> uniqueValues = new HashSet<CheckType>();
//        for (Object child : children) {
//            uniqueValues.add(selectionMap.get(child));
//        }
//        if (uniqueValues.size() == 1) {
//            return uniqueValues.iterator().next();
//        } else {
//            return PARTIALCHECK;
//        }
//    }

//    final Map<Object, CheckType> selectionMap = new HashMap<Object, CheckType>();

    public void fill() {

        Estateunit estateunit1 = new Estateunit(1, "Schweigaardsgate 61c");
        Estateunit estateunit2 = new Estateunit(2, "Schweigaardsgate 61d");

        Area area11 = new Area(1, "Tak");
        Area area12 = new Area(2, "Gang");
        Area area21 = new Area(3, "Vegg");
        Area area22 = new Area(4, "Trapp");

//        String node1 = "node1";
//        String node11 = "node11";
//        String node12 = "node12";
//        String node111 = "node111";
//        String node112 = "node112";
//
//        String node2 = "node2";
//        String node21 = "node21";
//        String node22 = "node22";

//        addContainerProperty("caption", String.class, "");
//        setItemCaptionPropertyId("caption");


        Item estateItem1 = addNode(estateunit1, estateunit1.getName(), true, true);
//        estateItem1.getItemProperty("caption").setValue(estateunit1.getName());

        Item areaItem1 = addNode(area11, area11.getName(), estateunit1, false, false);
//        areaItem1.getItemProperty("caption").setValue(area11.getName());

        Item areaItem2 = addNode(area12, area12.getName(), estateunit1, false, false);
//        areaItem2.getItemProperty("caption").setValue(area12.getName());

        Item estateItem2 = addNode(estateunit2, estateunit2.getName(), true, true);
//        estateItem2.getItemProperty("caption").setValue(estateunit2.getName());

        Item areaItem21 = addNode(area21, area21.getName(), estateunit2, false, false);
//        areaItem21.getItemProperty("caption").setValue(area21.getName());

        Item areaItem22 = addNode(area22, estateunit2.getName(), estateunit2, false, false);
//        areaItem22.getItemProperty("caption").setValue(area22.getName());


//        addNode(node1, true, true);
////        addItem(node1);
////        expandItem(node1);
//        addNode(node11, node1, true, true);
////        addItem(node11);
////        setParent(node11, node1);
////        expandItem(node11);
//        addNode(node111, node11, false, false);
//        addNode(node112, node11, false, false);
//        addNode(node12, node1, true, true);
//        addNode(node2, true, true);
////        addItem(node2);
////        expandItem(node2);
//        addNode(node21, node2, true, false);
//        addItem(node21);
//        setChildrenAllowed(node21, false);
//        setParent(node21, node2);
//        addNode(node22, node2, false, true);
    }

//    private Item addNode(Object itemId, String caption, boolean isChildrenAllowed, boolean expand) {
//        return addNode(itemId, caption, null, isChildrenAllowed, expand);
//    }
//
//    private Item addNode(Object itemId, String caption, Object parent, boolean isChildrenAllowed, boolean expand) {
//        Item item = addItem(itemId);
//        item.getItemProperty("caption").setValue(caption);
//        setParent(itemId, parent);
//        setChildrenAllowed(itemId, isChildrenAllowed);
//        if(expand){
//            expandItem(itemId);            
//        }
//        return item;
//    }
//
//    private Item addNode(String itemId, String parent, boolean isChildrenAllowed, boolean expand) {
//        Item item = addItem(itemId);
//        setParent(itemId, parent);
//        setChildrenAllowed(itemId, true);
//        expandItem(itemId);
//        return item;
//    }
//
//    ItemStyleGenerator itemStyleGenerator = new ItemStyleGenerator() {
//        public String getStyle(Object itemId) {
//            return selectionMap.get(itemId).getStyle();
//        }
//    };
//
//
//    @Override
//    public Item addItem(Object itemId) throws UnsupportedOperationException {
//        Item item = super.addItem(itemId);
//        selectionMap.put(itemId, UNCHECKED);
//        return item;
//    }
}
