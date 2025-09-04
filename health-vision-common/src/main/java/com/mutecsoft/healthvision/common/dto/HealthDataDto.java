package com.mutecsoft.healthvision.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mutecsoft.healthvision.common.deserializer.DoubleToLocalDateTimeDeserializer;
import com.mutecsoft.healthvision.common.model.health.MobileHrData;
import com.mutecsoft.healthvision.common.model.health.MobileSleepData;
import com.mutecsoft.healthvision.common.model.health.MobileWorkoutData;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthDataDto {
    
    private List<Workout> workouts;
    private List<Sleep> sleeps;
    private List<HeartRate> heartRates;

    @Getter
    @Setter
    public static class Workout {
        private double totalEnergyBurned;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private double duration;
        private String activityType;
        
        public MobileWorkoutData toModel() {
        	MobileWorkoutData model = new MobileWorkoutData();
        	model.setActivityType(this.activityType);
        	model.setDuration(this.duration);
        	model.setStartDt(this.startDate);
        	model.setEndDt(this.endDate);
        	model.setBurnedCalories(new BigDecimal(this.totalEnergyBurned));
        	return model;
        }
    }

    @Getter
    @Setter
    public static class Sleep {
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String category;
        
        public MobileSleepData toModel() {
        	MobileSleepData model = new MobileSleepData();
        	model.setCategory(this.category);
        	model.setStartDt(this.startDate);
        	model.setEndDt(this.endDate);
        	return model;
        }
    }

    @Getter
    @Setter
    public static class HeartRate {
    	@JsonDeserialize(using = DoubleToLocalDateTimeDeserializer.class)
        private LocalDateTime timestamp;
        private double value;
        
        public MobileHrData toModel() {
        	MobileHrData model = new MobileHrData();
        	model.setRecordDt(this.timestamp);
        	model.setValue(new BigDecimal(this.value));
        	return model;
        }
    }
	
}
