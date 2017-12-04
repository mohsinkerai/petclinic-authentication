package com.system.demo.volunteer.printer;

import com.system.demo.volunteer.Volunteer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Zeeshan Damani
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrintingResult {

    private String fileName;
    private List<Volunteer> printedVolunteers;
}
