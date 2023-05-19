package com.toddler.footsteps.heatmap

import android.os.Parcel
import android.os.Parcelable
import ca.hss.heatmaplib.HeatMap
import kotlinx.android.parcel.Parcelize


data class Points(
    var p0: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p1: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p2: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p3: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p4: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p5: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p6: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p7: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p8: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p9: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p10: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
    var p11: HeatMap.DataPoint = HeatMap.DataPoint(0.0F, 0.0F,0.0),
)
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader),
//        parcel.readParcelable(HeatMap.DataPoint::class.java.classLoader)
//
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeParcelable(p0, flags)
//        parcel.writeParcelable(p1, flags)
//        parcel.writeParcelable(p2, flags)
//        parcel.writeParcelable(p3, flags)
//        parcel.writeParcelable(p4, flags)
//        parcel.writeParcelable(p5, flags)
//        parcel.writeParcelable(p6, flags)
//        parcel.writeParcelable(p7, flags)
//        parcel.writeParcelable(p8, flags)
//        parcel.writeParcelable(p9, flags)
//        parcel.writeParcelable(p10, flags)
//        parcel.writeParcelable(p11, flags)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Points> {
//        override fun createFromParcel(parcel: Parcel): Points {
//            return Points(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Points?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
