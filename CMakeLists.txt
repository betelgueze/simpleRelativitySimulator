cmake_minimum_required(VERSION 3.0)

project(simulator)

add_executable(simulator main.cpp)

install(TARGETS simulator RUNTIME DESTINATION bin)
