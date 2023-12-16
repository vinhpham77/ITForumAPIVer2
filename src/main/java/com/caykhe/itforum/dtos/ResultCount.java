package com.caykhe.itforum.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultCount<T> {
    List<T> resultList;
    long count;

}
