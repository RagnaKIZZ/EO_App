package com.hard.eoapp.paket.paketmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ItemPaketItem implements Parcelable {

	@SerializedName("nama_item")
	private String namaItem;

	protected ItemPaketItem(Parcel in) {
		namaItem = in.readString();
	}

	public ItemPaketItem(){

	}

	public static final Creator<ItemPaketItem> CREATOR = new Creator<ItemPaketItem>() {
		@Override
		public ItemPaketItem createFromParcel(Parcel in) {
			return new ItemPaketItem(in);
		}

		@Override
		public ItemPaketItem[] newArray(int size) {
			return new ItemPaketItem[size];
		}
	};

	public void setNamaItem(String namaItem){
		this.namaItem = namaItem;
	}

	public String getNamaItem(){
		return namaItem;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(namaItem);
	}
}