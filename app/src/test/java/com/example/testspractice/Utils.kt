package com.example.testspractice

fun getPrivateField(obj: Any, fieldName: String): Any? {
    val field = obj.javaClass.getDeclaredField(fieldName)
    field.isAccessible = true
    return field.get(obj)
}