# Parakeet4J Demo

## Install Parakeet4J

This project use the [Parakeet4J](https://github.com/parakeet-nest/parakeet4j) library.

1- Create (or update) a file: `~/.m2/settings.xml` with the following content

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/parakeet-nest/parakeet4j</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <username>your GitHub handle</username>
      <password>your GitHub token</password>
    </server>
  </servers>
</settings>
```

2- Add this section to your `pom.xml` file:

```xml
<dependencies>
    <dependency>
        <groupId>org.parakeetnest.parakeet4j</groupId>
        <artifactId>parakeet4j</artifactId>
        <version>0.0.5</version>
    </dependency>
</dependencies>
```

### Remark: build with Docker and Docker Compose

> This is a work in progress

To build a Java project using the Parakeet4J library you need to copy `~/.m2/settings.xml` to the root of the project (at the same place of the `Dockerfile`)
> add `settings.xml` to `.gitignore`

