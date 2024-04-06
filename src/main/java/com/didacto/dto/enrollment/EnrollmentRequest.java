package com.didacto.dto.enrollment;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Enrollemnt : 강의 참여 요청")
public class EnrollmentRequest {

    @NotBlank(message = "참여 대상 강의가 입력되지 않았습니다.")
    @Schema(example = "1")
    private Long lectureId;

}
