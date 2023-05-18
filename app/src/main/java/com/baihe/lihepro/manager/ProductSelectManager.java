package com.baihe.lihepro.manager;

import android.text.TextUtils;

import com.baihe.lihepro.entity.CityEntity;
import com.baihe.lihepro.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Author：xubo
 * Time：2020-09-04
 * Description：
 */
public class ProductSelectManager extends Observable {
    private List<ProductEntity> selectProduct;

    public static class INSTANCE {
        private static final ProductSelectManager productSelectManager = new ProductSelectManager();
    }

    public static ProductSelectManager newInstance() {
        return ProductSelectManager.INSTANCE.productSelectManager;
    }

    private ProductSelectManager() {
        selectProduct = new ArrayList<>();
    }

    public List<ProductEntity> getSelectProduct() {
        return selectProduct;
    }

    public void rest(List<ProductEntity> selectProduct){
        this.selectProduct.clear();
        if(selectProduct!=null){
            this.selectProduct.addAll(selectProduct);
        }
        deleteObservers();
    }

    public void updateObservers() {
        setChanged();
        notifyObservers();
    }

    public void subscribe(ProductSelectCallback callback){
        addObserver(callback);
    }

    public static abstract class ProductSelectCallback implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            updateNotify();
        }

        public abstract void updateNotify();
    }
}
