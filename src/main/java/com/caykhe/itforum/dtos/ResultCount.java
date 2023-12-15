package com.caykhe.itforum.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ResultCount<T> {
    List<T> resultList;
    long count;

}
