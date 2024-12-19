package com.example.androidecommerceapp.view.orderHistory.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidecommerceapp.database.OrderEntity
import com.example.androidecommerceapp.view.orderHistory.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orders = MutableLiveData<List<OrderEntity>>()
    val orders: LiveData<List<OrderEntity>> get() = _orders

    fun addOrder(order: OrderEntity) {
        viewModelScope.launch {
            orderRepository.addOrder(order)
        }
    }

    fun getOrders() {
        viewModelScope.launch {
            orderRepository.getAllOrders().collect {
                _orders.postValue(it)
            }
        }
    }
}
