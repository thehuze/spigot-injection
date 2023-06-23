package com.hakan.test.command;

import com.google.inject.Inject;
import com.hakan.injection.command.annotations.Command;
import com.hakan.injection.command.annotations.CommandParam;
import com.hakan.injection.command.annotations.Executor;
import com.hakan.injection.command.annotations.Subcommand;
import com.hakan.test.service.TestService;
import org.bukkit.command.CommandSender;

@Command(
        name = "test",
        usage = "/test",
        aliases = {"test2"},
        description = "test command"
)
public class TestCommand {

    private final TestService service;

    @Inject
    public TestCommand(TestService service) {
        this.service = service;
    }

    @Subcommand
    public void test1(@Executor CommandSender executor,
                      @CommandParam String target,
                      @CommandParam String message,
                      @CommandParam int amount) {
        for (int i = 0; i < amount; i++) {
            this.service.sendMessage(target, message);
        }
    }

    @Subcommand(
            permission = "test.permission",
            permissionMessage = "You don't have permission to use this command."
    )
    public void test2(@Executor CommandSender executor,
                      @CommandParam String target,
                      @CommandParam String message) {
        this.service.sendMessage(target, message);
    }
}