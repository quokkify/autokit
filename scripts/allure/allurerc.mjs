/**
 * Allure Report 3 config for the merged quokkify CI report.
 * CI writes artifacts/allure-variables.json via scripts/ci/write-allure-environment.sh.
 */
import fs from "node:fs";
import path from "node:path";

const varsFile =
  process.env.ALLURE_VARIABLES_JSON ||
  path.join(process.cwd(), "artifacts/allure-variables.json");

/** @returns {Record<string, string>} */
function loadMergedVariablesRaw() {
  try {
    const raw = fs.readFileSync(varsFile, "utf8");
    const obj = JSON.parse(raw);
    if (obj && typeof obj === "object" && !Array.isArray(obj)) {
      return Object.fromEntries(
        Object.entries(obj).map(([k, v]) => [k, String(v)]),
      );
    }
  } catch {
    /* Missing or malformed variables should not block report generation. */
  }
  return {};
}

/**
 * Split merged CI variables into global and module-family buckets.
 * @param {Record<string, string>} merged
 */
function partitionMergedVariables(merged) {
  const global = {};
  const common = {};
  const data = {};
  const integrations = {};

  for (const [key, value] of Object.entries(merged)) {
    if (key.startsWith("Common.")) {
      common[key] = value;
    } else if (key.startsWith("Data.")) {
      data[key] = value;
    } else if (key.startsWith("Integrations.")) {
      integrations[key] = value;
    } else {
      global[key] = value;
    }
  }

  return { global, common, data, integrations };
}

/**
 * @param {{labels?: {name?: string, value?: string}[]}} result
 * @param {string} name
 */
function labelValue(result, name) {
  const labels = Array.isArray(result.labels) ? result.labels : [];
  return String(labels.find((label) => label.name === name)?.value || "");
}

/**
 * @param {string} value
 * @param {string[]} prefixes
 */
function startsWithAny(value, prefixes) {
  return prefixes.some((prefix) => value.startsWith(prefix));
}

/**
 * @param {{labels?: {name?: string, value?: string}[]}} result
 */
function isDataResult(result) {
  const parentSuite = labelValue(result, "parentSuite");
  const packageLabel = labelValue(result, "package");

  return (
    startsWithAny(parentSuite, ["data-utils@"]) ||
    startsWithAny(packageLabel, [
      "io.automation.test.Database",
      "io.automation.test.Redis",
    ])
  );
}

/**
 * @param {{labels?: {name?: string, value?: string}[]}} result
 */
function isIntegrationsResult(result) {
  const parentSuite = labelValue(result, "parentSuite");
  const packageLabel = labelValue(result, "package");

  return (
    startsWithAny(parentSuite, ["integrations@"]) ||
    startsWithAny(packageLabel, [
      "io.automation.kafka.",
      "io.automation.rabbitmq.",
      "io.automation.reportportal.",
      "io.automation.tyrus.",
    ])
  );
}

/**
 * @param {{labels?: {name?: string, value?: string}[]}} result
 */
function isCommonResult(result) {
  return !isDataResult(result) && !isIntegrationsResult(result);
}

const merged = loadMergedVariablesRaw();
const { global, common, data, integrations } = partitionMergedVariables(merged);

export default {
  name: "quokkify",
  output: "./allure-report",
  variables: global,
  environments: {
    common: {
      matcher: isCommonResult,
      variables: common,
    },
    data: {
      matcher: isDataResult,
      variables: data,
    },
    integrations: {
      matcher: isIntegrationsResult,
      variables: integrations,
    },
  },
  plugins: {
    awesome: {
      options: {
        reportName: "quokkify",
        singleFile: false,
        reportLanguage: "en",
        groupBy: ["epic", "feature", "story", "package"],
      },
    },
  },
};
