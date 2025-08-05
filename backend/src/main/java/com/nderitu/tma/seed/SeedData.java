package com.nderitu.tma.seed;

import lombok.Data;
import java.util.List;

@Data
public class SeedData {
    private List<SeedUser> users;
    private List<SeedTask> tasks;
} 