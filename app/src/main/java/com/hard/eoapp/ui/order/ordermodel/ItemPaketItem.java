package com.hard.eoapp.ui.order.ordermodel;

import com.google.gson.annotations.SerializedName;

public class ItemPaketItem{

	@SerializedName("nama_item")
	private String namaItem;

	public void setNamaItem(String namaItem){
		this.namaItem = namaItem;
	}

	public String getNamaItem(){
		return namaItem;
	}
}