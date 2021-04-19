/*
 * Copyright (c) 2020 Zavarov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package zav.discord.blanc.command.developer;

import org.atteo.evo.inflector.English;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import zav.discord.blanc._factory.MessageEmbedFactory;
import zav.discord.blanc.MessageEmbed;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.util.List;

/**
 * This command shows the status of the bot.
 */
public class StatusCommand extends StatusCommandTOP {
    /**
     * A constant for MebiByte to make the used memory easier to read.
     */
    protected final static int MEBI = 1 << 20;
    protected final static double GIGA = 1e9;

    private final MessageEmbed messageEmbed = MessageEmbedFactory.create();
    private final SystemInfo systemInfo = new SystemInfo();
    private final HardwareAbstractionLayer hardware = systemInfo.getHardware();

    @Override
    public void run() throws IOException {
        messageEmbed.clearFields();
        setTitle();
        printCpu();
        printSensors();
        printOs();
        printMemory();
        printJvm();
        get$MessageChannel().send(messageEmbed);
    }

    private void setTitle(){
        OperatingSystem os = systemInfo.getOperatingSystem();
        messageEmbed.setTitle(os.toString());
    }

    private void printCpu(){
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        CentralProcessor cpu = hardware.getProcessor();
        CentralProcessor.ProcessorIdentifier identifier = cpu.getProcessorIdentifier();
        List<CentralProcessor.LogicalProcessor> processors = cpu.getLogicalProcessors();
        long [] frequencies = cpu.getCurrentFreq();

        messageEmbed.addFields("CPU Name", identifier.getName());
        messageEmbed.addFields("Logical", cpu.getLogicalProcessorCount(), true);
        messageEmbed.addFields("Physical", cpu.getPhysicalProcessorCount(), true);
        messageEmbed.addFields("Available", os.getAvailableProcessors(), true);
        messageEmbed.addFields("System Load", 100.0 * cpu.getSystemLoadAverage(1)[0] / cpu.getLogicalProcessorCount() + "%", true);

        StringBuilder frequencyBuilder = new StringBuilder();
        for(int i = 0 ; i < Math.min(processors.size(), frequencies.length) ; ++i)
            frequencyBuilder.append(String.format("`[%-2d] %.2f`\n", processors.get(i).getProcessorNumber(), frequencies[i]/GIGA));

        messageEmbed.addFields("Frequency (GHz)", frequencyBuilder.toString(), true);
    }

    private void printSensors(){
        Sensors sensors = hardware.getSensors();
        messageEmbed.addFields("CPU Temperature (°C)", sensors.getCpuTemperature(), true);
        messageEmbed.addFields("CPU Voltage (V)", sensors.getCpuVoltage(), true);
    }

    private void printOs(){
        OperatingSystem os = systemInfo.getOperatingSystem();
        OSProcess process = os.getProcess(os.getProcessId());
        Duration duration = Duration.ofMillis(process.getUpTime());
        messageEmbed.addFields(
                "Uptime",
                String.format(
                        "Running for %d %s, %d %s and %d %s.\n",
                        duration.toDays(),English.plural("day",(int)duration.toDays()),
                        duration.toHours() % 24, English.plural("hour",(int)(duration.toHours() % 24)),
                        duration.toMinutes() % 60,English.plural("minute",(int)(duration.toMinutes() % 60))
                ),
                false
        );
    }

    private void printJvm(){
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        messageEmbed.addFields("JVM", runtime.getSpecName());
        messageEmbed.addFields("Vendor", runtime.getSpecVendor(), true);
        messageEmbed.addFields("Version", runtime.getSpecVersion(), true);
    }

    private void printMemory(){
        GlobalMemory memory = hardware.getMemory();
        OperatingSystem os = systemInfo.getOperatingSystem();
        OSProcess process = os.getProcess(os.getProcessId());
        long total = memory.getTotal() / MEBI;
        long free = memory.getAvailable() / MEBI;
        long used = process.getResidentSetSize() / MEBI;
        double ratio = (100.0 * used) / total;

        String memoryMessage = "`Total | Used  | Free  | Ratio`\n" +
                String.format("`%-5d | %-5d | %-5d | %-4.1f%%`\n", total, used, free, ratio);
        messageEmbed.addFields("Global Memory", memoryMessage, false);
    }
}
