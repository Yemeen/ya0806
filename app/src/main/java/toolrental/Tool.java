package toolrental;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Tool {
    private static final Map<String, Tool> TOOL_MAP = Arrays.stream(new Tool[] {
            new Tool("LADW", ToolType.LADDER, "Werner"),
            new Tool("CHNS", ToolType.CHAINSAW, "Stihl"),
            new Tool("JAKD", ToolType.JACKHAMMER, "DeWalt"),
            new Tool("JAKR", ToolType.JACKHAMMER, "Ridgid")
    }).collect(Collectors.toMap(Tool::getCode, Function.identity()));

    private final String code;
    private final ToolType type;
    private final String brand;

    private Tool(String code, ToolType type, String brand) {
        this.code = code;
        this.type = type;
        this.brand = brand;
    }

    public String getCode() {
        return code;
    }

    public ToolType getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public double getDailyCharge() {
        return type.getDailyCharge();
    }

    public boolean isWeekdayCharge() {
        return type.isWeekdayCharge();
    }

    public boolean isWeekendCharge() {
        return type.isWeekendCharge();
    }

    public boolean isHolidayCharge() {
        return type.isHolidayCharge();
    }

    public static Optional<Tool> fromCode(String code) {
        return Optional.ofNullable(code)
                .map(String::trim)
                .map(String::toUpperCase)
                .map(TOOL_MAP::get);
    }

    @Override
    public String toString() {
        return "Tool{code=" + code + ", type=" + type + ", brand=" + brand + ", dailyCharge=" + getDailyCharge() + "}";
    }
}
