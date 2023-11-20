package com.wanted.restaurant.boundedContext.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LunchDTO {
	private String name;
	private String address;
	private Double grade;
	private String type;

	public static LunchDTO of(LunchDTOInterface lunchDTOInterface) {
		return new LunchDTO(lunchDTOInterface.getName(), lunchDTOInterface.getAddress(), lunchDTOInterface.getGrade(),
			lunchDTOInterface.getType());
	}

	public static interface LunchDTOInterface{
		String getName();
		String getAddress();
		Double getGrade();
		String getType();
	}
}
