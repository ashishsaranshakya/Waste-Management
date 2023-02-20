package dev.ashishshakya.wastemanagement;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name;
    private String material;
    private String closestHub;
    private String methodToRecycle_alternativeUse;
    private String localResourcesAvailable;
    private boolean recycleable;
    private String imageUrl;

    public Item() {
    }

    public Item(String name, String material, String closestHub, String methodToRecycle_alternativeUse, String localResourcesAvailable, boolean recycleable,String imageUrl) {
        this.name = name;
        this.material = material;
        this.recycleable = recycleable;
        this.closestHub = closestHub;
        this.methodToRecycle_alternativeUse = methodToRecycle_alternativeUse;
        this.localResourcesAvailable = localResourcesAvailable;
        this.imageUrl=imageUrl;
    }

    public Item(Map<String,Object> map){
        this.name = (String) map.get("name");
        this.material = (String) map.get("material");
        this.recycleable = (boolean) map.get("recycleable");
        this.closestHub = (String) map.get("closestHub");
        this.methodToRecycle_alternativeUse = (String) map.get("methodToRecycle_alternativeUse");
        this.localResourcesAvailable = (String) map.get("localResourcesAvailable");
        this.imageUrl=(String) map.get("imageUrl");
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("material",material);
        map.put("closestHub",closestHub);
        map.put("methodToRecycle_alternativeUse",methodToRecycle_alternativeUse);
        map.put("localResourcesAvailable",localResourcesAvailable);
        map.put("recycleable",recycleable);
        map.put("imageUrl",imageUrl);
        return map;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public boolean isRecycleable() {
        return recycleable;
    }

    public void setRecycleable(boolean recycleable) {
        this.recycleable = recycleable;
    }

    public String getClosestHub() {
        return closestHub;
    }

    public void setClosestHub(String closestHub) {
        this.closestHub = closestHub;
    }

    public String getMethodToRecycle_alternativeUse() {
        return methodToRecycle_alternativeUse;
    }

    public void setMethodToRecycle_alternativeUse(String methodToRecycle_alternativeUse) {
        this.methodToRecycle_alternativeUse = methodToRecycle_alternativeUse;
    }

    public String getLocalResourcesAvailable() {
        return localResourcesAvailable;
    }

    public void setLocalResourcesAvailable(String localResourcesAvailable) {
        this.localResourcesAvailable = localResourcesAvailable;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", material='" + material + '\'' +
                ", closestHub='" + closestHub + '\'' +
                ", methodToRecycle_alternativeUse='" + methodToRecycle_alternativeUse + '\'' +
                ", localResourcesAvailable='" + localResourcesAvailable + '\'' +
                ", recycleable=" + recycleable +
                '}';
    }
}
