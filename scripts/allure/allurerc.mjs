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
 * @param {string[]} prefixes
 */
function packageStartsWith(result, prefixes) {
  const labels = Array.isArray(result.labels) ? result.labels : [];
  const packageLabel =
    labels.find(({ name }) => name === "package")?.value || "";
  return prefixes.some((prefix) => String(packageLabel).startsWith(prefix));
}

const merged = loadMergedVariablesRaw();
const { global, common, data, integrations } = partitionMergedVariables(merged);

export default {
  name: "quokkify",
  output: "./allure-report",
  variables: global,
  environments: {
    common: {
      matcher: (result) =>
        packageStartsWith(result, ["io.quokkify.common", "io.automation"]),
      variables: common,
    },
    data: {
      matcher: (result) => packageStartsWith(result, ["io.quokkify.data"]),
      variables: data,
    },
    integrations: {
      matcher: (result) =>
        packageStartsWith(result, ["io.quokkify.integrations"]),
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
